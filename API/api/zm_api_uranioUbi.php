<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    
    include_once("../Config/db.php");
    include_once("../Classes/usuarios.php");

    $objDB = new Database();
    $db = $objDB->getConnection();
    
    $data = json_decode(file_get_contents("php://input"));

    $idUsuario = $data->idUsuario;
    $est = $data->estado;
    $mun = $data->municipio;
    $est = str_replace(' ','_', $est);
    $mun = str_replace(' ','_', $mun);

    $objUsuarios = new usuarios($db);

    $stmt = $objUsuarios->updateUbicacion($idUsuario, $est, $mun);

    if($stmt && $idUsuario > 0){
        echo json_encode(
            array(
                "status"=> 1
            )
        );
        http_response_code(200);
    }else{
        http_response_code(404);
        echo json_encode(
            array("status"=> 0)
        );
    }

?>