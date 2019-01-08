<?php
define("DB_HOST", "localhost");
define("DB_USER", "id2558345_root");
define("DB_PASSWORD", "korea0504");
define("DB_DATABASE", "id2558345_kiki");

$con = mysqli_connect(DB_HOST,DB_USER, DB_PASSWORD, DB_DATABASE);

$Name = $_GET['_corname'];
$_Latitude = $_GET['_Latitude'];
$_Longitude = $_GET['_Longitude'];

function distance($lat1, $lon1, $lat2, $lon2, $unit){
  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));

  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);
 
  if ($unit == "K") {
    return ($miles * 1.609344 * 1609.344);
  } else if ($unit == "N") {
    return ($miles * 0.8684 * 1609.344);
  } else {
    return ($miles * 1609.344);
  }
}


$result = mysqli_query($con, "select _corname, _phone, _number, _address from vendor_info where (_corname like '%$Name' or _corname like '$Name%' or _corname like '%$Name%' )");

$response = array();

while($row = mysqli_fetch_array($result)){
    $vendor_ID = $row[0];
    $outID = $con->prepare("SELECT _number FROM vendor_info WHERE _corname=?");
    $outID->bind_param("s", $vendor_ID);
    $outID->execute();
    $temp = $outID->get_result()->fetch_assoc();
    $outID->close();

    $address = urlencode($row[3]);

    $sGoogleMapApi = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&language=ko&address=$address";

    $budget74 = file_get_contents($sGoogleMapApi);
    $resp = json_decode($budget74, true);

    $lat = $resp['results'][0]['geometry']['location']['lat'];
    $lng = $resp['results'][0]['geometry']['location']['lng'];

    $dis = distance($lat, $lng, $_Latitude, $_Longitude, "M");

    $IndexState = $con->prepare("SELECT _state FROM text_event WHERE _number=?");
    $IndexState->bind_param("s", $temp["_number"]);
   	$IndexState->execute();
   	$temp = $IndexState->get_result()->fetch_assoc();
   	$IndexState->close();

    array_push($response, array("_corname"=>$row[0], "_phone"=>$row[1], "_number"=>$row[2], "shopState"=>$temp['_state'], "distance"=>$dis));
}

echo json_encode(array("response"=>$response));
mysqli_close($con);
?>

