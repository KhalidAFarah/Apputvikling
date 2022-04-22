<?php
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
$connection = mysqli_connect('localhost', 'dbuser8', 'Childkingidea8{', 'studdb8');

$id = (String) $_REQUEST['Id'];
$beskrivelse = (String) $_REQUEST['Beskrivelse'];
$etasjer = (String) $_REQUEST['Etasjer'];

$sql = "UPDATE Hus SET beskrivelse='$beskrivelse', etasjer='$etasjer' WHERE id = '$id';";
mysqli_query($connection, $sql);
mysqli_close($connection);
?>