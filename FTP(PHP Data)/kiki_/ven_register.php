<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['phone']) && isset($_POST['email']) && isset($_POST['corname'])
&& isset($_POST['name'])&& isset($_POST['password'])&& isset($_POST['number']) && isset($_POST['birth']) && isset($_POST['address']) 
&& isset($_POST['follow']) && isset($_POST['gendor']) && isset($_POST['terms']) && isset($_POST['collect'])) {

    // receiving the post params
    $phone = $_POST['phone'];
    $email = $_POST['email'];
    $number = $_POST['number'];    
    $corname = $_POST['corname'];
    $name = $_POST['name'];
    $birth = $_POST['birth'];
    $password = $_POST['password'];
    $gendor = $_POST['gendor'];
    $address = $_POST['address'];
    $follow = $_POST['follow'];
    $terms = $_POST['terms'];
    $collect = $_POST['collect'];


    // check if user is already existed with the same number
    if ($db->isUserExisted($number)) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User already existed with " . $number;
        echo json_encode($response);
    } else {
        // create a new user
        $user = $db->storeUser($phone, $email, $number, $corname, $name, $password, $birth, $gendor, $address, $follow, $terms, $collect);
        if ($user) {
            // user stored successfully
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["phone"] = $user["_phone"];
            $response["user"]["email"] = $user["_email"];
            $response["user"]["number"] = $user["_number"];
            $response["user"]["corname"] = $user["_corname"];
            $response["user"]["name"] = $user["_name"];
            $response["user"]["birth"] = $user["_birth"];
            $response["user"]["gendor"] = $user["_gendor"];
            $response["user"]["address"] = $user["_address"];
            $response["user"]["follow"] = $user["_follow"];

            $response["user"]["created_at"] = $user["created_at"];

            echo json_encode($response);
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

