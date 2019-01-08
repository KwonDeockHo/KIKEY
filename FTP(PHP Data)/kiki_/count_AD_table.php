<?php

	$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");



	$result = mysqli_query($con, "select * from vendor_advertisement where _number");
	$response = array();


	$i=0;
	while($row = mysqli_fetch_array($result)){
	    array_push($response, array("_number_check$i"=>$row[0]));
	    $i++;
	}

	echo json_encode(array("response"=>$response));
	mysqli_close($con);

?>

