<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    
    include_once("../Config/db.php");
    include_once("../Classes/elecciones.php");

    $objDB = new Database();
    $db = $objDB->getConnection();
    
    $estado = "";
    $municipio = "";
    
    if(isset($_GET['estado']) && isset($_GET['municipio'])){
        $estado = $_GET['estado'];
        $municipio = $_GET['municipio'];
    }

    $objElecciones = new elecciones($db);

    $stmtF = $objElecciones->getFederales();
    $stmtE = $objElecciones->getEstatales($estado);
    $stmtM = $objElecciones->getMunicipales($estado, $municipio);

    $totalEleccionesF = $stmtF->rowCount();
    $totalEleccionesE = $stmtE->rowCount();
    $totalEleccionesM = $stmtM->rowCount();

    if($totalEleccionesF > 0 || $totalEleccionesE > 0 || $totalEleccionesM > 0){
        $array = array();
        while($row = $stmtF->fetch(PDO::FETCH_ASSOC)){
            extract($row);
            $e = array(
                "idEleccion" => $idEleccion,
                "tipo" => $tipo,
                "nombreEleccion" => $nombreEleccion,
                "estado" => $estado,
                "municipio" => $municipio
            );
            array_push($array, $e);
        }
        while($row = $stmtE->fetch(PDO::FETCH_ASSOC)){
            extract($row);
            $e = array(
                "idEleccion" => $idEleccion,
                "tipo" => $tipo,
                "nombreEleccion" => $nombreEleccion,
                "estado" => $estado,
                "municipio" => $municipio
            );
            array_push($array, $e);
        }
        while($row = $stmtM->fetch(PDO::FETCH_ASSOC)){
            extract($row);
            $e = array(
                "idEleccion" => $idEleccion,
                "tipo" => $tipo,
                "nombreEleccion" => $nombreEleccion,
                "estado" => $estado,
                "municipio" => $municipio
            );
            array_push($array, $e);
        }
        echo json_encode($array);
    }else{
        http_response_code(404);
        echo json_encode(
            array("message"=> "No se encontraron elecciones disponibles.")
        );
    }

?>