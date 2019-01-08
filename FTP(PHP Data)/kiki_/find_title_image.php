<?php

$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");
//$con = mysqli_connect("localhost","root", "1234", "kiki");


$Name = $_GET['_number'];

$result = mysqli_query($con, "select title_image from vendor_product_info where _number = '$Name'");

$response = array();


while($row = mysqli_fetch_array($result)){
    array_push($response, array("title_image"=>$row[0]));
}

echo json_encode(array("response"=>$response));
mysqli_close($con);

?>