#!/usr/bin/env python3
import sys
import os
import json
import re
import smtplib
from email.message import EmailMessage

def send_response(status_code, data):
    """Outputs HTTP headers and JSON payload."""
    status_messages = {
        200: "200 OK",
        400: "400 Bad Request",
        405: "405 Method Not Allowed",
        500: "500 Internal Server Error"
    }
    
    print(f"Status: {status_messages.get(status_code, '200 OK')}")
    print("Content-Type: application/json; charset=UTF-8")
    print("Access-Control-Allow-Origin: *")
    print("Access-Control-Allow-Methods: POST, OPTIONS")
    print("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With")
    print()  # End of HTTP headers
    
    print(json.dumps(data))
    sys.exit(0)

def sanitize_string(val):
    """Basic string sanitization equivalent to HTML special chars encoding."""
    if not val:
        return ""
    return (str(val).strip()
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace('"', "&quot;")
            .replace("'", "&#039;"))

def is_valid_email(email):
    """Validates email format using regular expressions."""
    pattern = r'^[\w\.-]+@[\w\.-]+\.\w+$'
    return re.match(pattern, email) is not None

def main():
    request_method = os.environ.get("REQUEST_METHOD", "GET").upper()

    # Preflight CORS handler
    if request_method == "OPTIONS":
        send_response(200, {})

    # Method restriction
    if request_method != "POST":
        send_response(405, {
            "success": False,
            "message": "Method Not Allowed. Only POST requests are permitted."
        })

    # Read payload from standard input
    try:
        content_length = int(os.environ.get("CONTENT_LENGTH", 0))
        raw_body = sys.stdin.read(content_length)
    except (ValueError, TypeError):
        raw_body = ""

    # Parse JSON or fallback to URL-encoded form data
    input_data = {}
    if raw_body:
        try:
            input_data = json.loads(raw_body)
        except json.JSONDecodeError:
            import urllib.parse
            parsed_form = urllib.parse.parse_qs(raw_body)
            # parse_qs puts values in lists, extract first element
            input_data = {k: v[0] for k, v in parsed_form.items() if v}

    # Extract & Sanitize Inputs
    name = sanitize_string(input_data.get("sender-name", ""))
    email = input_data.get("sender-email", "").strip()
    topic = sanitize_string(input_data.get("consulting-topic", ""))
    message_body = sanitize_string(input_data.get("message-body", ""))

    # Validation
    errors = {}
    if not name:
        errors["name"] = "Name or Organization is required."
    if not email or not is_valid_email(email):
        errors["email"] = "A valid email address is required."
    if not topic:
        errors["consulting"] = "Please select a topic."
    if not message_body:
        errors["message"] = "Message content cannot be blank."

    if errors:
        send_response(400, {
            "success": False,
            "message": "Validation failed. Please correct the highlighted fields.",
            "errors": errors
        })

    # Process Email Dispatch
    to_address = "jim.pham@totsllc.com"
    subject = f"Consultation Request: {topic.capitalize()} - {name}"
    
    email_content = (
        "You received a new message via totsllc.com:\n\n"
        f"Name: {name}\n"
        f"Email: {email}\n"
        f"Topic: {topic}\n\n"
        f"Message:\n{message_body}\n"
    )

    msg = EmailMessage()
    msg.set_content(email_content)
    msg["Subject"] = subject
    msg["From"] = email
    msg["To"] = to_address
    msg["Reply-To"] = email

    try:
        # Sends email using local sendmail host. Adjust server details if needed.
        with smtplib.SMTP("localhost") as server:
            server.send_message(msg)
        
        send_response(200, {
            "success": True,
            "message": f"Thank you, {name}. Your inquiry has been sent successfully."
        })
    except Exception:
        send_response(500, {
            "success": False,
            "message": "Unable to send message due to a server error. Please email directly."
        })

if __name__ == "__main__":
    main()