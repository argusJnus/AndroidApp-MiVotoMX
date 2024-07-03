<?php
    class Database{
        private $host = "localhost";
        private $db = "xxxx";
        private $username= "xxxx";
        private $pass = "xxxx";

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
