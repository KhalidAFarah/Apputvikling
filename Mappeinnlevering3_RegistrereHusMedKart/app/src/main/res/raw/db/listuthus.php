<?php
mysqli_report(MYSQLI_REPORT_ERROR | MYSQLI_REPORT_STRICT);
$connection = mysqli_connect('localhost', 'dbuser8', 'Childkingidea8{', 'studdb8');
$sql = ('SELECT * FROM Hus');
$tabell = mysqli_query($connection, $sql);
mysqli_close($connection);

while($row=mysqli_fetch_assoc($tabell)){
$output[] = $row;
}
print(json_encode($output));
?>