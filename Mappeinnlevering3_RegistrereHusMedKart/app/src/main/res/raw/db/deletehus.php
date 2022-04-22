<?php
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
$connection = mysqli_connect('localhost', 'dbuser8', 'Childkingidea8{', 'studdb8');

$id = (String) $_REQUEST['Id'];

mysqli_query($connection, "DELETE FROM Hus WHERE id = '$id';");
mysqli_close($connection);
?>