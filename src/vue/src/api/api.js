import axios from 'axios';
import util from '../common/js/util';


let base = 'http://localhost';

export const ajax = (url , method  , headers , params , calller) => {
    console.info(params);
    return axios({
        "method":method || "post",
        "url":url,
        "params":params,
        "data":params,
        "headers" :headers || {
            'Content-Type':'application/x-www-form-urlencoded'
        }
    }).then(function(res){
        console.info(res);
        var msg = null;
        if(!res.data){
            msg = "系统错误";

        }else if(res.data.code != "success"){
            if(res.data.code == "login"){
                calller.$router.push({ path: '/login' });
                return;
            }
            msg = res.data.msg || "未知错误";
        }

        if (msg){
            if(calller){

                util.Msg.error(calller  ,msg);
            }
            return;
        }


        return res.data;
    }).catch(function (error) {
        console.log(error);
        if(calller){

            util.Msg.error(calller ,"系统错误");
        }
    });
};


export const requestLogin = (params,caller) => {
    return ajax("/user/login" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const setting = (params,caller) => {
    return ajax("/user/setting" , null ,null ,params , caller);
};


export const listUser = (params,caller) => {
    return ajax("/user/list" , null ,null ,params , caller);
};
export const addUser = (params,caller) => {
    return ajax("/user/add" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const oneUser = (params,caller) => {
    return ajax("/user/one" , null ,null ,params , caller);
};
export const editUser = (params,caller) => {
    return ajax("/user/edit" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const delUser = (params,caller) => {
    return ajax("/user/del" , null ,null ,params , caller);
};


export const allRoom = (params,caller) => {
    return ajax("/room/all" , null ,null ,params , caller);
};
export const listRoom = (params,caller) => {
    return ajax("/room/list" , null ,null ,params , caller);
};
export const oneRoom = (params,caller) => {
    return ajax("/room/one" , null ,null ,params , caller);
};
export const addRoom = (params,caller) => {
    return ajax("/room/add" , null ,null ,params , caller);
};
export const editRoom = (params,caller) => {
    return ajax("/room/edit" , null ,null ,params , caller);
};
export const delRoom = (params,caller) => {
    return ajax("/room/del" , null ,null ,params , caller);
};



export const allAcct = (params,caller) => {
    return ajax("/acct/all" , null ,null ,params , caller);
};
export const addAcct = (params,caller) => {
    return ajax("/acct/add" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const oneAcct = (params,caller) => {
    return ajax("/acct/one" , null ,null ,params , caller);
};
export const chargeAcct = (params,caller) => {
    return ajax("/acct/charge" , "GET" ,null ,params , caller);
};
export const editAcct = (params,caller) => {
    return ajax("/acct/edit" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const listAcct = (params,caller) => {
    return ajax("/acct/list" , null ,null ,params , caller);
};
export const delAcct = (params,caller) => {
    return ajax("/acct/del" , null ,null ,params , caller);
};

export const listRecord = (params,caller) => {
    return ajax("/record/list" , null ,null ,params , caller);
};
















export const editData = (params,caller) => {
    return ajax("/data/edit" , null ,{
        'Content-Type':'application/json'
    } ,params , caller);
};
export const listData = (params,caller) => {
    return ajax("/data/list" , "GET" ,null ,params , caller);
};
export const delData = (params,caller) => {
    return ajax("/data/del" , null ,null ,params , caller);
};


export const listUserData = (params,caller) => {
    return ajax("/userData/list" , "GET" ,null ,params , caller);
};
export const download = (params,caller) => {
    axios.get(`/userData/download`, { //url: 接口地址
        params: params,
        responseType: `blob` //一定要写

    }).then(res => {
            if (res.status == 200) {
                let blob = new Blob([res.data], {
                 type: 'application/vnd.ms-excel'
                    //type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'
                    //word文档为application/msword,pdf文档为application/pdf,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8
                });
                let objectUrl = URL.createObjectURL(blob);
                let link = document.createElement("a");
                let fname = `匹配数据.xls`; //下载文件的名字
                link.href = objectUrl;
                link.setAttribute("download", fname);
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            } else {
                this.$message({
                    type: "error",
                    message: "导出失败"
                })
            }
        })
};



export const queryAchieve = (params,caller) => {
    return ajax(`/achievement/query` , "POST" , {
            'Content-Type': 'multipart/form-data'
        }
        ,params , caller);
};
export const downloadAchieve = (params,caller) => {
    axios.get(`/achievement/download`, { //url: 接口地址
        params: params,
        responseType: `blob` //一定要写

    }).then(res => {
        if (res.status == 200) {
            let blob = new Blob([res.data], {
                type: 'application/vnd.ms-excel'
                //type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'
                //word文档为application/msword,pdf文档为application/pdf,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8
            });
            let objectUrl = URL.createObjectURL(blob);
            let link = document.createElement("a");
            let fname = `匹配数据.xls`; //下载文件的名字
            link.href = objectUrl;
            link.setAttribute("download", fname);
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        } else {
            this.$message({
                type: "error",
                message: "导出失败"
            })
        }
    })
};