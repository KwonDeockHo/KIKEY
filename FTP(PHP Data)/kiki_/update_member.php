<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['phone']) && isset($_POST['email']) && isset($_POST['name'])&& isset($_POST['password']) && isset($_POST['birth']) && isset($_POST['gendor']) && isset($_POST['address']) && isset($_POST['follow']) ) 
{

    // receiving the post params
    $phone = $_POST['phone'];
    $email = $_POST['email'];
    $name = $_POST['name'];
    $password = $_POST['password'];
    $birth = $_POST['birth'];
    $gendor = $_POST['gendor'];
    $address = $_POST['address'];
    $follow = $_POST['follow'];

    // create a new user
    $user = $db->member_update($phone, $email, $name, $password, $birth, $gendor, $address, $follow);
    if ($user) {
        // user stored successfully
        $response["error"] = FALSE;
        $response["uid"] = $user["unique_id"];
        $response["user"]["phone"] = $user["_phone"];
        $response["user"]["email"] = $user["_email"];
        $response["user"]["name"] = $user["_name"];
        $response["user"]["birth"] = $user["_birth"];
        $response["user"]["gendor"] = $user["_gendor"];
        $response["user"]["address"] = $user["_address"];
        $response["user"]["follow"] = $user["_follow"];
        $response["error_msg"] = "User already existed with 1";

         echo json_encode($response);
        } 
        else {
            // user failed to store
             $response["error_msg"] = "User already existed with 2";

            $response["error"] = TRUE;
            echo json_encode($response);
        }

} else {
    $response["error"] = TRUE;
        $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>

