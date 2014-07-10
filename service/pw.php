<?php
// phpinfo();
define('PW_AUTH', 'LqmbcCgZdiUVK8RgyEwtkLDoeqkKe0FsqxWjL15xkO0u6RHky4JI2QjrVPe1P1eqeJWsICmnEmMGwVrWYzlU');
define('PW_APPLICATION', 'C90F5-1B040');
define('PW_DEBUG', true);
 
function pwCall($method, $data) {
    $url = 'https://cp.pushwoosh.com/json/1.3/' . $method;
    $request = json_encode(array('request' => $data));
    print_r($request);
    $ch = curl_init($url);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, true);
    curl_setopt($ch, CURLOPT_ENCODING, 'gzip, deflate');
    curl_setopt($ch, CURLOPT_HEADER, true);
    curl_setopt($ch, CURLOPT_POST, true);
    curl_setopt($ch, CURLOPT_POSTFIELDS, $request);
 
    $response = curl_exec($ch);
    $info = curl_getinfo($ch);
    curl_close($ch);
 
    // if (defined('PW_DEBUG') && PW_DEBUG) {
    //     print "[PW] request: $request\n";
    //     print "[PW] response: $response\n";
    //     print "[PW] info: " . print_r($info, true);
    // }
}
 
 
pwCall('createMessage', array(
    'application' => PW_APPLICATION,
    'auth' => PW_AUTH,
    'notifications' => array(
            array(
                'send_date' => 'now',
                'content' => 'test',
                'data' => array('custom' => 'json data'),
                'link' => 'http://pushwoosh.com/',
                "android_header"=> "header"/*,
                "devices"=> array("dec301908b9ba8df85e57a58e40f96f523f4c2068674f5fe2ba25cdc250a2a41")*/
            )
        )
    )
);
?>