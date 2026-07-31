#!/usr/bin/perl
use strict;
use warnings;
use CGI;
use JSON;
use HTML::Entities;
use Email::Valid;

my $cgi = CGI->new;

sub send_response {
    my ($status, $data_ref) = @_;
    
    print $cgi->header(
        -type => 'application/json; charset=UTF-8',
        -status => $status,
        -access_control_allow_origin => '*',
        -access_control_allow_methods => 'POST, OPTIONS',
        -access_control_allow_headers => 'Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With'
    );
    
    print encode_json($data_ref);
    exit;
}

my $request_method = $ENV{'REQUEST_METHOD'} || 'GET';

# Preflight CORS handler
if ($request_method eq 'OPTIONS') {
    send_response('200 OK', {});
}

# Method restriction
if ($request_method ne 'POST') {
    send_response('405 Method Not Allowed', {
        success => \0,
        message => 'Method Not Allowed. Only POST requests are permitted.'
    });
}

# Parse incoming request body
my %input_data;
my $raw_body = $cgi->param('POSTDATA');

if ($raw_body) {
    eval {
        my $decoded = decode_json($raw_body);
        %input_data = %{$decoded} if ref $decoded eq 'HASH';
    };
}

# Fallback to standard URL-encoded POST params if JSON parsing failed
if (!%input_data) {
    foreach my $param ($cgi->param()) {
        $input_data{$param} = $cgi->param($param);
    }
}

# Extract & Sanitize Inputs
my $name    = encode_entities($input_data{'sender-name'} // '');
my $email   = $input_data{'sender-email'} // '';
my $topic   = encode_entities($input_data{'consulting-topic'} // '');
my $message = encode_entities($input_data{'message-body'} // '');

# Trim whitespace
$name =~ s/^\s+|\s+$//g;
$email =~ s/^\s+|\s+$//g;
$topic =~ s/^\s+|\s+$//g;
$message =~ s/^\s+|\s+$//g;

# Validation
my %errors;
if (!$name) {
    $errors{'name'} = 'Name or Organization is required.';
}
if (!$email || !Email::Valid->address($email)) {
    $errors{'email'} = 'A valid email address is required.';
}
if (!$topic) {
    $errors{'consulting'} = 'Please select a topic.';
}
if (!$message) {
    $errors{'message'} = 'Message content cannot be blank.';
}

if (%errors) {
    send_response('400 Bad Request', {
        success => \0,
        message => 'Validation failed. Please correct the highlighted fields.',
        errors  => \%errors
    });
}

# Process Email Dispatch
my $to      = 'jim.pham@totsllc.com';
my $subject = 'Consultation Request: ' . ucfirst($topic) . ' - ' . $name;

my $email_body = "You received a new message via totsllc.com:\n\n";
$email_body  .= "Name: $name\n";
$email_body  .= "Email: $email\n";
$email_body  .= "Topic: $topic\n\n";
$email_body  .= "Message:\n$message\n";

# Using sendmail binary directly (standard Perl approach)
my $sendmail = "/usr/sbin/sendmail -t";
if (open(my $fh, '|-', $sendmail)) {
    print $fh "To: $to\n";
    print $fh "From: $email\n";
    print $fh "Reply-To: $email\n";
    print $fh "Subject: $subject\n\n";
    print $fh $email_body;
    close $fh;

    send_response('200 OK', {
        success => \1,
        message => "Thank you, $name. Your inquiry has been sent successfully."
    });
} else {
    send_response('500 Internal Server Error', {
        success => \0,
        message => 'Unable to send message due to a server error. Please email directly.'
    });
}