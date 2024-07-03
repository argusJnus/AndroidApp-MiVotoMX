<?php

    class elecciones{
            private $conn;
            private $tabla = "elecciones";
    
            public function __construct($db){
                $this->conn = $db;
            }
            
            //Devuelve la elección estatal disponible para el usuario
            public function getEstatales($estado){
                $statement = "SELECT * FROM ".$this->tabla." WHERE estado = :estado AND tipo = 2 AND status = 0";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":estado", $estado);
                $query->execute();
                return $query;
            }
            
            //Devuelve la elección municipal disponible para el usuario
            public function getMunicipales($estado, $municipio){
                $statement = "SELECT * FROM ".$this->tabla." WHERE municipio = :municipio AND estado = :estado AND tipo = 3 AND status = 0";
                $query = $this->conn->prepare($statement);
                $query->bindParam(":municipio", $municipio);
                $query->bindParam(":estado", $estado);
                $query->execute();
                return $query;
            }

            //Devuelve la elección federal
            public function getFederales(){
                $statement = "SELECT * FROM ".$this->tabla." WHERE tipo = 1 AND status = 0";
                $query = $this->conn->prepare($statement);
                $query->execute();
                return $query;
            }

            
    }

?>