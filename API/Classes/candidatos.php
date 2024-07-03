<?php

    class candidatos{
            private $conn;
            private $tabla = "candidatos";
    
            public function __construct($db){
                $this->conn = $db;
            }
            
            //Devuelve los candidatos de una elección de acuerdo al id de la Elección
            public function getCandidatos($idElecciones){
                $statement = "SELECT * FROM ".$this->tabla." WHERE idElecciones = :idElecciones";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":idElecciones", $idElecciones);
                $query->execute();
                return $query;
            }

            //Agrega un voto al candidato correspondiente
            public function updateVotos($id){
                $statement = "UPDATE $this->tabla SET votos = votos + 1 WHERE idCandidato = :id";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":id", $id);
                return ($query->execute())?true : false;
            }
    }

?>