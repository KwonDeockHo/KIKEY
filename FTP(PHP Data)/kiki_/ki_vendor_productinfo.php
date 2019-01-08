<?php
    
	$con = mysqli_connect("localhost","id2558345_root", "korea0504", "id2558345_kiki");

    $response = array("error" => FALSE);

    if (isset($_POST['_number']) && isset($_POST['title_image']) && isset($_POST['product_data1']) &&
    isset($_POST['product_data2']) && isset($_POST['product_data3']) && isset($_POST['product_data4']) &&
    isset($_POST['product_data5']) && isset($_POST['product_data6']) && isset($_POST['_index']))
    {

        $_number = $_POST["_number"];
        $title_image = $_POST["title_image"];
        $product_data1 = $_POST["product_data1"];
        $product_data2 = $_POST["product_data2"];
        $product_data3 = $_POST["product_data3"];
        $product_data4 = $_POST["product_data4"];
        $product_data5 = $_POST["product_data5"];
        $product_data6 = $_POST["product_data6"];
        $_index = $_POST["_index"];

        

        $statement = $con->prepare("insert into vendor_product_info values (?,?,?,?,?,?,?,?,?)");
        $statement->bind_param("sssssssss", $_number, $title_image, $product_data1, $product_data2, $product_data3, $product_data4, $product_data5, $product_data6, $_index);
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