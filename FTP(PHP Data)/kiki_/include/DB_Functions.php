<?php

/**
 * @author Ravi Tamada
 * @link http://www.androidhive.info/2012/01/android-login-and-registration-with-php-mysql-and-sqlite/ Complete tutorial
 */

class DB_Functions {
    private $conn;

    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new DB_Connect();
        $this->conn = $db->connect();
    }

    // destructor
    function __destruct() {
        
    }

    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($_phone, $_email, $_number, $_corname, $_name, $_password, $_birth, $_gendor, $_address, $_follow, $_terms, $_collect) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($_password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO vendor_info(unique_id, _phone, _email, _number, _corname, 
_name, encrypted_password, _birth, _gendor, _address, _follow, _terms, _collect, salt, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, NOW())");
        $stmt->bind_param("ssssssssssssss", $uuid, $_phone, $_email, $_number, $_corname, $_name, $encrypted_password, $_birth, $_gendor, $_address, $_follow, $_terms, $_collect, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM vendor_info WHERE _email = ?");
            $stmt->bind_param("s", $_email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
    public function isUser_text_event($_number) {
        $stmt = $this->conn->prepare("SELECT _number from text_event WHERE _number = ?");
        $stmt->bind_param("s", $_number);
        $stmt->execute();
        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    public function new_text_event($_number, $_state, $_value) 
    {
        $stmt = $this->conn->prepare("INSERT INTO text_event(_number, _state, _value) VALUES(?, ?, ?)");
        $stmt->bind_param("sss", $_number, $_state, $_value);
        
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM text_event WHERE _number = ?");
            $stmt->bind_param("s", $_number);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
    public function update_text_event($_number, $_state, $_value) 
    {
         $stmt = $this->conn->prepare("UPDATE text_event SET _state=?, _value=? WHERE _number=?");
        $stmt->bind_param("sss", $_state, $_value, $_number);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM text_event WHERE _number = ?");
            $stmt->bind_param("s", $_number);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
    public function search_vendor($_corname) 
    {
        $stmt = $this->conn->prepare("SELECT * FROM vendor_info WHERE _corname = ?");
        $stmt->bind_param("s", $_corname);
        $stmt->execute();
        $user = $stmt->get_result()->fetch_assoc();
        $stmt->close();

        // check for successful store
        if ($user) {
            return $user;
        } else {
            return false;
        }
    }

    public function search_text_event($_number) 
    {
        $stmt = $this->conn->prepare("SELECT * FROM text_event WHERE _number = ?");
        $stmt->bind_param("s", $_number);
        $stmt->execute();
        $event = $stmt->get_result()->fetch_assoc();
        $stmt->close();

        // check for successful store
        if ($event) {
            return $event;
        } else {
            return false;
        }
    }








public function member_storeUser($_phone, $_email, $_name, $_password, $_birth, $_gendor, $_address, $_follow, $_terms, $_collect) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($_password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("INSERT INTO member_info(unique_id, _phone, _email, _name, encrypted_password, _birth, _gendor, _address, _follow, _terms, _collect, salt, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?, NOW())");
        $stmt->bind_param("ssssssssssss", $uuid, $_phone, $_email, $_name, $encrypted_password, $_birth, $_gendor, $_address, $_follow, $_terms, $_collect, $salt);
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM member_info WHERE _email = ?");
            $stmt->bind_param("s", $_email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
public function member_update($_phone, $_email, $_name, $_password, $_birth, $_gendor, $_address, $_follow) {
        $hash = $this->hashSSHA($_password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("UPDATE member_info SET _phone=?, _name=?, encrypted_password=?,_birth=?, _gendor=?,_address=?,salt=? WHERE _email=?");
        $stmt->bind_param("ssssssss", $_phone, $_name, $encrypted_password, $_birth, $_gendor, $_address, $salt, $_email); 
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM member_info WHERE _email = ?");
            $stmt->bind_param("s", $_email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
public function vendor_update($_phone, $_email, $_number, $_corname, $_name, $_password, $_birth, $_gendor, $_address, $_follow) {
        $hash = $this->hashSSHA($_password);
        $encrypted_password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt

        $stmt = $this->conn->prepare("UPDATE vendor_info SET _phone=?, _name=?, encrypted_password=?,_birth=?, _gendor=?,_address=?,salt=? WHERE _email=?");
        $stmt->bind_param("ssssssss", $_phone, $_name, $encrypted_password, $_birth, $_gendor, $_address, $salt, $_email); 
        $result = $stmt->execute();
        $stmt->close();

        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM vendor_info WHERE _email = ?");
            $stmt->bind_param("s", $_email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            return $user;
        } else {
            return false;
        }
    }
    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM vendor_info WHERE _email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
    public function isCheckEmail($email) {
        $stmt = $this->conn->prepare("SELECT _email from vendor_info WHERE _email = ?");

        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    public function getMemberByEmailAndPassword($email, $password) {

        $stmt = $this->conn->prepare("SELECT * FROM member_info WHERE _email = ?");

        $stmt->bind_param("s", $email);

        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();

            // verifying user password
            $salt = $user['salt'];
            $encrypted_password = $user['encrypted_password'];
            $hash = $this->checkhashSSHA($salt, $password);
            // check for password equality
            if ($encrypted_password == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }

    public function isMemCheckEmail($email) {
        $stmt = $this->conn->prepare("SELECT _email from member_info WHERE _email = ?");

        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->store_result();
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    /**
     * Check user is existed or not
     */

    public function isUserExisted($number) {
        $stmt = $this->conn->prepare("SELECT _number from vendor_info WHERE _number = ?");

        $stmt->bind_param("s", $number);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    
    public function isMemberExisted($phone) {
        $stmt = $this->conn->prepare("SELECT _phone from member_info WHERE _phone = ?");

        $stmt->bind_param("s", $phone);

        $stmt->execute();

        $stmt->store_result();

        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {

        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }

    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {

        $hash = base64_encode(sha1($password . $salt, true) . $salt);

        return $hash;
    }

}

?>
