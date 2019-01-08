<?php
    
$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");

$response = array("error" => FALSE);

if (isset($_POST['_number']) && isset($_POST['ad_point']) && isset($_POST['ad_image1']) &&
    isset($_POST['ad_image2']) && isset($_POST['ad_image3']) && isset($_POST['ad_image4']) &&
    isset($_POST['ad_image5']) && isset($_POST['ad_image6']))
{

    $_number = $_POST["_number"];
    $ad_point = $_POST["ad_point"];
    $ad_image1 = $_POST["ad_image1"];
    $ad_image2 = $_POST["ad_image2"];
    $ad_image3 = $_POST["ad_image3"];
    $ad_image4 = $_POST["ad_image4"];
    $ad_image5 = $_POST["ad_image5"];
    $ad_image6 = $_POST["ad_image6"];

    $statement = $con->prepare("INSERT INTO vendor_advertisement() VALUES (?,?,?,?,?,?,?,?)");
    $statement->bind_param("ssssssss", $_number, $ad_point, $ad_image1, $ad_image2, $ad_image3, $ad_image4, $ad_image5, $ad_image6);
    $result = $statement->execute();

    if($result){
        $response = array();
        $response["success"] = true;
        echo json_encode($response);
    }
}else{
    $response["success"] = false;
    echo json_encode($response);
}



?>