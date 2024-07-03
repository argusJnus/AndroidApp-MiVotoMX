<?php

    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    include_once("../Config/db.php");
    include_once("../Classes/usuarios.php");

    $data = json_decode(file_get_contents("php://input"));
    $correo = $data->correo;
    $passw = $data->pass;
 
    $objDB = new Database();
    $db = $objDB->getConnection();

    $objUsers = new usuarios($db);

    $stmt = $objUsers->readUsuario($correo);
    $userExist = $stmt->rowCount();

    if($userExist > 0){
        $array = array();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        extract($row);
        if(password_verify($passw,$pass)){
            echo json_encode(
                array(
                    "status"=> 1,
                    "idUsuario"=> $idUsuario,
                    "estado"=> $estado,
                    "municipio"=> $municipio,
                    "fed"=> $fed,
                    "est"=> $est,
                    "mun"=> $mun
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
        
    }else{
        echo json_encode(
            array(
                "status"=> 2
            )
        );
    }

?>