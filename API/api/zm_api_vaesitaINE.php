<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    include_once("../Config/db.php");
    include_once("../Classes/usuarios.php");

    $data = json_decode(file_get_contents("php://input"));
    $ine = $data->ine;
    $idUsuario = $data->idUsuario;
    
    $objDB = new Database();
    $db = $objDB->getConnection();

    $objUsers = new usuarios($db);

    $stmt = $objUsers->getUser($idUsuario);
    $user = $stmt->fetch(PDO::FETCH_ASSOC);
    extract($user);
    if(password_verify($ine, $zm_ine)){
        echo json_encode(
            array(
                "status"=> 1
            )
        );
    }else{
        echo json_encode(
            array(
                "status"=> 0,
                "message"=> "Contraseña incorrecta."
            )
        );
    }

?>