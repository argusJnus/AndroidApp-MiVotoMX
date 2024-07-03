<?php

    class usuarios{
            private $conn;
            private $tabla = "usuarios";
    
            public function __construct($db){
                $this->conn = $db;
            }
    
            public function getUsuarios(){
                $statement = "SELECT * FROM ".$this->tabla;
                $query = $this->conn->prepare($statement);
                $query->execute();
                return $query;
            
            }
            
            //Obtiene usuario a partir de su ID
            public function getUser($idUsuario){
                $statement = "SELECT * FROM ".$this->tabla." WHERE idUsuario = :idUsuario";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":idUsuario", $idUsuario);
                $query->execute();
                return $query;
            }

            //Valida usuario con correo y contraseña para logear
            public function validarUsuario($correo, $pass){
                $statement = "SELECT * FROM ".$this->tabla." WHERE correo = :correo AND pass = :pass";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":correo", $correo);
                $query->bindParam(":pass", $pass);
                $query->execute();
                return $query;
            }

            //Obtiene usuario de acuerdo al correo proporcionado
            public function readUsuario($correo){
                $statement = "SELECT * FROM ".$this->tabla." WHERE correo = :correo";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":correo", $correo);
                $query->execute();
                return $query;
            }
            
            //Registra un usuario
            public function insertUsuario($curp, $fec_nacimiento, $estado, $municipio, $correo, $pass, $ine){
                $statement = "INSERT INTO $this->tabla (
                                curp,
                                fec_nacimiento,
                                estado,
                                municipio,
                                correo,
                                pass,
                                zm_ine,
                                fed,
                                est,
                                mun
                            ) VALUES (
                                :curp,
                                :fec,
                                :estado,
                                :municipio,
                                :correo,
                                :pass,
                                :ine,
                                0,
                                0,
                                0
                            )";
                $pass = password_hash($pass, PASSWORD_DEFAULT);
                $ine = password_hash($ine, PASSWORD_DEFAULT);
                $query = $this->conn->prepare($statement);
                $query->bindParam(":curp",$curp);
                $query->bindParam(":fec",$fec_nacimiento);
                $query->bindParam(":estado",$estado);
                $query->bindParam(":municipio",$municipio);
                $query->bindParam(":correo",$correo);
                $query->bindParam(":pass",$pass);
                $query->bindParam(":ine",$ine);
                
                if($query->execute()){
                    return true;
                }else{
                    return false;
                }
            }
            
            public function updateVoto($tipo, $id){
                switch($tipo){
                    case 1: $statement = "UPDATE $this->tabla SET fed = 1 WHERE idUsuario = :id";
                            break;
                    case 2: $statement = "UPDATE $this->tabla SET est = 1 WHERE idUsuario = :id";
                            break;
                    case 3: $statement = "UPDATE $this->tabla SET mun = 1 WHERE idUsuario = :id";
                            break;
                }
                $query = $this->conn->prepare($statement);
                $query->bindParam(":id", $id);
                return ($query->execute())?true : false;
            }
            
            //Actualiza la ubicacion
            public function updateUbicacion($idUsuario, $est, $mun){
                $statement = "UPDATE $this->tabla SET estado = :est, municipio = :mun WHERE idUsuario = :id";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":id", $idUsuario);
                $query->bindParam(":est", $est);
                $query->bindParam(":mun", $mun);
                return ($query->execute())?true : false;
            }
            
            //Valida si el usuario ya existe
            public function userExist($correo, $curp){
                $statement = "SELECT COUNT(*) as count FROM ".$this->tabla." WHERE curp = :curp or correo = :correo";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":correo", $correo);
                $query->bindParam(":curp", $curp);
                $query->execute();
                $result = $query->fetch(PDO::FETCH_ASSOC);
                return $result['count'];
            }

    }

?>
