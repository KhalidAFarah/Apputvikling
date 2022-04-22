<?php
$connection = mysqli_connect('localhost', 'dbuser8', 'Childkingidea8{', 'studdb8');

$adresse = (String) $_REQUEST['Adresse'];
$latitude = (String) $_REQUEST['Latitude'];
$longitude = (String) $_REQUEST['Longitude'];
$beskrivelse = (String) $_REQUEST['Beskrivelse'];
$etasjer = (String) $_REQUEST['Etasjer'];

mysqli_query($connection, "INSERT INTO Hus (adresse, latitude, longitude, beskrivelse, etasjer) VALUES ('$adresse', '$latitude', '$longitude', '$beskrivelse', '$etasjer');");
$id = $connection->insert_id;
$sql = ("SELECT * FROM Hus WHERE id='$id'");
$res = mysqli_query($connection, $sql);
mysqli_close($connection);
print(json_encode(mysqli_fetch_assoc($res)));
?>