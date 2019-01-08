<?php

$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");


$Name = $_GET['_corname'];

$result = mysqli_query($con, "select * from vendor_product_info where _number = (select _number from vendor_info where _corname ='$Name')");

$response = array();


while($row = mysqli_fetch_array($result)){
    array_push($response, array("title_image"=>$row[1], "product_data1"=>$row[2], "product_data2"=>$row[3], "product_data3"=>$row[4], "product_data4"=>$row[5], "product_data5"=>$row[6], "product_data6"=>$row[7], "_index"=>$row[8]));
}

echo json_encode(array("response"=>$response));
mysqli_close($con);

?>
