<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['email_check'])) 
{
    $email = $_POST['email_check'];


    // get the user by email
    if ($db->isMemCheckEmail($email)) 
    {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = true;
        $response["error_position"] = 1;
        echo json_encode($response);
    }else{
        $user = $db->isMemCheckEmail($email);
        if($user){
            $response["user"]["email"] = $user["_email"];
    	    $response["error"] = false;
            $response["error_msg"] = false;
                $response["error_position"] = 2;
            echo json_encode($response);
        }else{
            $response["error"] = TRUE;
            $response["error_msg"] = false;
            $response["error_position"] = 3;
            echo json_encode($response);  
        }
    }
}else{
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = false;
    $response["error_position"] = 4;

    echo json_encode($response);
}

?>