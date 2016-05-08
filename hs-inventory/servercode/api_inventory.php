<?php
/**
 * Created by PhpStorm.
 * User: lizelin
 * Date: 16/3/29
 * Time: 上午10:51
 */

if (__FILE__ == '')
{
    die('Fatal error code: 0');
}

define('IN_ECS', true);

$root_path = str_replace('/mobile/imodia/inventory/api_inventory.php', '', str_replace('\\', '/', __FILE__)).'/mobile/';
require_once($root_path . 'includes/init.php');
require_once($root_path . 'includes/cls_json.php');
// oracle db
require_once($root_path . 'includes/ez_sql_core.php');
require_once($root_path . 'includes/ez_sql_oracle8_9.php');
include_once "ez_sql_core.php";

$backenddb = new ezSQL_oracle8_9('backend','----','oradb1');

$json = new JSON;
$ret_data = array();

$act = $_GET['act'];

if (empty($act)) {
    $ret_data['code'] = '500';
    $ret_data['message'] = '参数非法！';
    die(unicodeDecode($json->encode($ret_data)));
}

if ($act=='login') {
    $useraccount = $_POST['useraccount'];
    $password = $_POST['password'];
    if (empty($useraccount) || empty($password)) {
        $ret_data['code'] = '500';
        $ret_data['message'] = '用户名密码均不可为空！';
        die(unicodeDecode($json->encode($ret_data)));
    }
    $password = md5($password);

    $sql = "select count(*) from pss_inventory_account where vc2account='{$useraccount}' and vc2password = '{$password}' and vc2enabledflag='Y'";
    $rs = $backenddb->get_var($sql);
    if (!empty($rs)) {
        $_SESSION['pss_useraccount'] = $useraccount;
        $ret_data['code'] = '200';
        $ret_data['message'] = '登录成功！';
        print_r(unicodeDecode($json->encode($ret_data)));
        exit;
    } else {
        $ret_data['code'] = '500';
        $ret_data['message'] = '用户名或者密码错误！';
        die(unicodeDecode($json->encode($ret_data)));
    }
} elseif ($act=='checklogin') {
    if (empty($_SESSION['pss_useraccount'])) {
        $ret_data['code'] = '403';
        $ret_data['message'] = '尚未登录！';
    } else {
        $ret_data['code'] = '200';
        $ret_data['message'] = '已登录！';
    }
    print_r(unicodeDecode($json->encode($ret_data)));
    exit;
} elseif ($act=='logout') {
    $_SESSION['pss_useraccount']='';
    unset($_SESSION['pss_useraccount']);
    $ret_data['code'] = '200';
    $ret_data['message'] = '操作成功！';
    print_r(unicodeDecode($json->encode($ret_data)));
    exit;
} elseif ($act=='get_init_data') {
    checkLogin();
    $useraccount = $_SESSION['pss_useraccount'];
    $sql = "select vc2guid, vc2account, vc2warehouseguid, vc2warehousetype, vc2warehousename from pss_inventory_acc_ref_wh where vc2account = '{$_SESSION['pss_useraccount']}'";
    $whs = $backenddb->get_results($sql);
    $sql = "select t.vc2mdse_code, t.vc2mdse_name, t.vc2mdse_sku from pss_merchandise t where t.vc2enabledflag = 'Y'";
    $skus = $backenddb->get_results($sql);

    $sql =  " select w.vc2guid, w.vc2account, t.vc2mdse_code, t.numstock_count ".
            "   from pss_wh_mdse_stock t, pss_inventory_acc_ref_wh w " .
            "   where t.vc2warehouse_guid = w.vc2warehouseguid " .
            "   and t.vc2warehouse_type = w.vc2warehousetype and t.numstock_count>0 " .
            "   and w.vc2account = '{$useraccount}'";
    $stocks = $backenddb->get_results($sql);
    $ret_data['code'] = '200';
    $ret_data['message'] = '操作成功！';
    $ret_data['whs'] = $whs;
    $ret_data['skus'] = $skus;
    $ret_data['stocks'] = $stocks;
    print_r(unicodeDecode($json->encode($ret_data)));
    exit;
} elseif ($act=='load_data') {
    checkLogin();

    $email = $_POST['email'];
    $data = $_POST['data'];

    $emailserver = "http://----/jsp/api/mailsend.jsp";
    $params = array();
    /**
     * {
    "recipient" : "zhangchunjin@imodia.com",
    "subject" : "测试邮件",
    "content" : "这是一封测试邮件a",
    "from" : "hs360"
    }
     *
     * {"statusCode":1,"errmsg":"成功"}
     */
    $params['recipient'] = $email;
    $params['subject'] = "盘点数据(请将邮件内容复制到文本文件，并且保存为.csv文件，用excel打开进行编辑)";
    $params['content'] = str_replace('\\"','"', $data);
    if (strpos($params['content'], "\n")) {
        LibUtils::logAppClient("include enter");
    }
    $params['content'] = str_replace("\n","<br/>\n", $params['content']);

    $params['from'] = "from_inventory_app";

    $ret_data['code'] = '500';
    $ret_data['message'] = 'email发送失败！';
    LibUtils::logAppClient("email is :" . $email);
    LibUtils::logAppClient("data is :" . $data);
    LibUtils::logAppClient("encode is :" .$json->encode($params));

    $result = LibUtils::http_post($emailserver, urlencode($json->encode($params)));
    LibUtils::logAppClient("result is :" . $result);

    if ($result) {
//        $result_json = $json->decode($result, 0);
//        LibUtils::logAppClient("result_json is :");
//        LibUtils::logAppClient($result_json);

        $result_json = json_decode($result, true);
        LibUtils::logAppClient("result_json is :");
        LibUtils::logAppClient($result_json);

        if ($result_json && !empty($result_json['statusCode']) && $result_json['statusCode']==1) {
            $ret_data['code'] = '200';
            $ret_data['message'] = '操作成功！';
        }
    }


    print_r(unicodeDecode($json->encode($ret_data)));
    exit;
}


$ret_data['code'] = '500';
$ret_data['message'] = '未知错误！';
die(unicodeDecode($json->encode($ret_data)));

function checkLogin() {
    if (empty($_SESSION['pss_useraccount'])) {
        $json = new JSON;
        $ret_data = array();
        $ret_data['code'] = '403';
        $ret_data['message'] = '尚未登录！';
        die(unicodeDecode($json->encode($ret_data)));
    }

}

function unicodeDecode($data)
{
    /*
    function replace_unicode_escape_sequence($match) {
        return mb_convert_encoding(pack('H*', $match[1]), 'UTF-8', 'UCS-2BE');
    }
    $rs = preg_replace_callback('/\\\\u([0-9a-f]{4})/i', 'replace_unicode_escape_sequence', $data);

    //去掉反斜线
    $rs=stripslashes($rs);
    return $rs;
    */
    return $data;
}