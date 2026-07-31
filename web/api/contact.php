<?php
// api/contact.php

header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: POST, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

// Preflight CORS handler
if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(200);
    exit();
}

// Method restriction
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode([
        "success" => false,
        "message" => "Method Not Allowed. Only POST requests are permitted."
    ]);
    exit();
}

// Parse incoming request JSON
$inputData = json_decode(file_get_contents("php://input"), true);
if (!$inputData) {
    $inputData = $_POST;
}

const NAME = "name";
const EMAIL = "email";
const CONSULTING = "consulting";
const MSG = "message";

// Extract & Sanitize Inputs
$name        = isset($inputData[NAME]) ? trim(filter_var($inputData[NAME], FILTER_SANITIZE_SPECIAL_CHARS)) : '';
$email       = isset($inputData[EMAIL]) ? trim(filter_var($inputData[EMAIL], FILTER_SANITIZE_EMAIL)) : '';
$topic       = isset($inputData[CONSULTING]) ? trim(filter_var($inputData[CONSULTING], FILTER_SANITIZE_SPECIAL_CHARS)) : '';
$messageBody = isset($inputData[MSG]) ? trim(filter_var($inputData[MSG], FILTER_SANITIZE_SPECIAL_CHARS)) : '';

// Validation
$errors = [];
if (empty($name)) {
    $errors[NAME] = "Name or Organization is required.";
}

if (empty($email) || !filter_var($email, FILTER_VALIDATE_EMAIL)) {
    $errors[EMAIL] = "A valid email address is required.";
}

if (empty($topic)) {
    $errors[CONSULTING] = "Please select a topic."; 
}

if (empty($messageBody)) {
    $errors[MSG] = "Message content cannot be blank."; 
}

if (!empty($errors)) {
    http_response_code(400);
    echo json_encode([
        "success" => false,
        "message" => "Validation failed. Please correct the highlighted fields.",
        "errors"  => $errors
    ]);
    exit();
}

// Process Email Dispatch
$to          = "jim.pham@totsllc.com";
$fromAddress = "noreply@totsllc.com"; // Must match your domain to align with SPF/DKIM
$subject     = "Consultation Request: " . ucfirst($topic) . " - " . $name;

// Construct Headers compliant with domain authentication
$headers   = [];
$headers[] = "From: " . $name . " <" . $fromAddress . ">";
$headers[] = "Reply-To: " . $email; // Preserves 1-click reply to the visitor
$headers[] = "MIME-Version: 1.0";
$headers[] = "Content-Type: text/plain; charset=UTF-8";
$headers[] = "X-Mailer: PHP/" . phpversion();

$headersString = implode("\r\n", $headers);

$emailBody   = "You received a new message via totsllc.com:\n\n";
$emailBody  .= "Name: " . $name . "\n";
$emailBody  .= "Email: " . $email . "\n";
$emailBody  .= "Topic: " . $topic . "\n\n";
$emailBody  .= "Message:\n" . $messageBody . "\n";

// The 5th parameter (-f) sets the envelope sender for SPF validation
$mailSent = @mail($to, $subject, $emailBody, $headersString, "-f" . $fromAddress);

if ($mailSent) {
    http_response_code(200);
    echo json_encode([
        "success" => true,
        "message" => "Thank you, " . $name . ". Your inquiry has been sent successfully."
    ]);
} else {
    http_response_code(500);
    echo json_encode([
        "success" => false,
        "message" => "Unable to send message due to a server error. Please email directly."
    ]);
}
?>