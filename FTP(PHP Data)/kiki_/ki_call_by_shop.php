<?php

	$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");

	$Name = $_GET['_corname'];

	$result = mysqli_query($con, "select _phone from vendor_info where _corname like '$Name'");
	$response = array();


	while($row = mysqli_fetch_array($result)){
	    array_push($response, array("_phone"=>$row[0]));
	}

	echo json_encode(array("response"=>$response));
	mysqli_close($con);

?>
