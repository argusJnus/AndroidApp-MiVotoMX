<?php
    //satus 2 significa que correo está en uso
    header("Access-Control-Allow-Origin:");
    header("Content-Type: application/json; charset=UTF-8");
    header("Access-Control-Allow-Methods: POST");
    header("Access-Control-Max-Age: 3600");
    header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

    include_once("../Config/db.php");
    include_once("../Classes/usuarios.php");

    $objDB = new Database();
    $db = $objDB->getConnection();

    $objUsuarios = new usuarios($db);
    
    $data = json_decode(file_get_contents("php://input"));

    $curp = $data->curp;
    $fec_nacimiento = $data->fec_nacimiento;
    $estado = $data->estado;
    $municipio = $data->municipio;

    $estado = str_replace(' ', '_', $estado);
    $municipio = str_replace(' ', '_', $municipio);

    $ine = $data->ine;
    $correo = $data->correo;
    $pass = $data->pass;
    $stmt = $objUsuarios->userExist($correo, $curp);
    $userExist = $stmt;
    if($userExist == 0){

        if($objUsuarios->insertUsuario($curp, $fec_nacimiento, $estado, $municipio, $correo, $pass, $ine)){
            echo json_encode(
                array(
                    "status"=> 1
                )
            );
            http_response_code(200);
        }else{
            echo json_encode(
                array(
                    "status"=> 0
                )
            );
            http_response_code(404);
        }
    }else{
        echo json_encode(
            array(
                "status"=> 2
            )
        );
        http_response_code(404);
    }

?>