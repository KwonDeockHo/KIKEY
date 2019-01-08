<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if ( isset($_POST['number']) && isset($_POST['state']) && isset($_POST['value']) )
{

    // receiving the post params
    $_number = $_POST['number'];
    $_state = $_POST['state'];
    $_value = $_POST['value'];    

    // create a new user
    if ($db->isUser_text_event($_number)) {
        $user = $db->update_text_event($_number, $_state, $_value);
        if($user){
            $response["error"] = false;
            $response["error_msg"] = "Update Success";
            echo json_encode($response);
        }else{
            $response["error"] = true;
            $response["error_msg"] = "Update Fail";
            echo json_encode($response);
        }
        // user stored successfully
    }else{
        $user = $db->new_text_event($_number, $_state, $_value);
        if($user){
        $response["error"] = false;
        $response["error_msg"] = "Insert Success";
        echo json_encode($response);
        } 
        else {
        $response["error"] = true;
        $response["error_msg"] = "Insert Fail";
        echo json_encode($response);
        }
    }
} 
else{
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

