<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

require_once 'include/DB_Functions.php';
$db = new DB_Functions();

// json response array
$response = array("error" => FALSE, "state"=>FALSE);

if (isset($_POST['email']) && isset($_POST['password'])) {

    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];

    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
    $member = $db->getMemberByEmailAndPassword($email, $password);

    if ($user != FALSE) {
        // use is found
        $response["error"] = FALSE;
        $response["state"] = TRUE;
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

    } else if($member != FALSE){
        $response["error"] = FALSE;
        $response["state"] = FALSE;
        $response["uid"] = $member["unique_id"];
        $response["user"]["phone"] = $member["_phone"];
        $response["user"]["email"] = $member["_email"];
        $response["user"]["name"] = $member["_name"];
        $response["user"]["birth"] = $member["_birth"];
        $response["user"]["address"] = $member["_address"];
        $response["user"]["follow"] = $member["_follow"];
        $response["user"]["gendor"] = $member["_gendor"];
        $response["user"]["created_at"] = $member["created_at"];
        echo json_encode($response);

    } else{
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["state"] = TRUE;
        $response["error_msg"] = "아이디 혹은 비밀번호가 맞지 않습니다!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["state"] = TRUE;
    $response["error_msg"] = "아이디가 존재하지 않습니다!";
    echo json_encode($response);
}
?>

