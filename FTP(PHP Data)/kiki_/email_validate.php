<?php

	require_once 'include/DB_Functions.php';
	$db = new DB_Functions();

	$_email = $_POST["_email"];

	$statement = mysqli_prepare($conn, "select * from vendor_info where _email = ?");
	mysqli_stmt_bind_param($statement, "s", $_email);
	mysqli_stmt_execute($statement);
	mysqli_stmt_store_result($statement);
	mysqli_stmt_bind_result($statement, $_email);

	$response = array();
	$response["success"] = true;

	while(mysqli_stmt_fetch($statement)){
		$response["success"] = false;
		$response["_email"] = $_email;
	}

	echo json_encode($response);

?>