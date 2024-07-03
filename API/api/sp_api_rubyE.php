<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    
    include_once("../Config/db.php");
    include_once("../Classes/candidatos.php");

    $objDB = new Database();
    $db = $objDB->getConnection();
    
    $idEleccion = "";
    
    if(isset($_GET['idEleccion'])){
        $idEleccion = $_GET['idEleccion'];
    }

    $objCandidatos = new candidatos($db);

    $stmt = $objCandidatos->getCandidatos($idEleccion);

    $totalCandidatos = $stmt->rowCount();

    if($totalCandidatos > 0){
        $array = array();
        while($row = $stmt->fetch(PDO::FETCH_ASSOC)){
            extract($row);
            $e = array(
                "idCandidato" => $idCandidato,
                "nombre" => $nombre,
                "partido" => $partido,
                "idElecciones" => $idElecciones
            );
            array_push($array, $e);
        }
        echo json_encode($array);
    }else{
        http_response_code(404);
        echo json_encode(
            array("message"=> "No se encontraron candidatos.")
        );
    }

?>