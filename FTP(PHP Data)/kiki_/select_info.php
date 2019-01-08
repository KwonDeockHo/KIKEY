<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['corname'])) 
{
    // receiving the post params
    $corname = $_POST['corname'];

        // create a new user
        $user = $db->search_vendor($corname);
        $number = $user["_number"];
        $event = $db->search_text_event($number);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;

            $response["select"]["phone"] = $user["_phone"];
            $response["select"]["address"] = $user["_address"];
            $response["select"]["event_state"] = $event["_state"];
            $response["select"]["event_value"] = $event["_value"];
            $response["error_msg"] = "Success!";
            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>