<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    
    include_once("../Config/db.php");
    include_once("../Classes/candidatos.php");
    include_once("../Classes/usuarios.php");

    $objDB = new Database();
    $db = $objDB->getConnection();
    
    $data = json_decode(file_get_contents("php://input"));

    $idUsuario = $data->idUsuario;
    $tipo = $data->tipo;
    $idCandidato = $data->idCandidato;

    $objCandidatos = new candidatos($db);
    $objUsuario = new usuarios($db);

    $stmt = $objCandidatos->updateVotos($idCandidato);
    $stmtUser = $objUsuario->updateVoto($tipo,$idUsuario);
    $usuario = $objUsuario->getUser($idUsuario);
    
    if($stmt && $stmtUser){
        $user = $usuario->fetch(PDO::FETCH_ASSOC);
        extract($user);
        
        echo json_encode(
            array(
                "status" => 1,
                "fed" => $fed,
                "est" => $est,
                "mun" => $mun 
            )
        );
        
    }else{
        http_response_code(404);
        echo json_encode(
            array("message"=> "Error")
        );
    }

?>