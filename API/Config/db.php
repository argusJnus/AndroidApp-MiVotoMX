<?php
    class Database{
        private $host = "localhost";
        private $db = "id21769232_mivotomx";
        private $username= "id21769232_mivotomx";
        private $pass = "@mvMX_bd32";

        public $conn;

        public function getConnection(){
            $this->conn = null;
            try{
                $this->conn = new PDO("mysql:host=".$this->host.";dbname=".$this->db,$this->username,$this->pass);
                $this->conn->exec("set names utf8");
                return $this->conn;
            }catch(PDOException $e){
                echo "Error: ".$e->getMessage();    
            }
        }
    }

?>