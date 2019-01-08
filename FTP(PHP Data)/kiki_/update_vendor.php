<?php

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE);

if (isset($_POST['phone']) && isset($_POST['email']) && isset($_POST['corname'])
&& isset($_POST['name'])&& isset($_POST['password'])&& isset($_POST['number']) && isset($_POST['birth']) && isset($_POST['address']) 
&& isset($_POST['follow']) && isset($_POST['gendor']) ) 
{

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

    // create a new user
    $user = $db->vendor_update($phone, $email, $number, $corname, $name, $password, $birth, $gendor, $address, $follow);
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

