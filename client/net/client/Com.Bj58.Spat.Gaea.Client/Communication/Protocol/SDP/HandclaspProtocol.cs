using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using WWW58COM.SPAT.SCF.Serializer.Component.Attributes;

namespace WWW58COM.SPAT.SCF.Client.Communication.Protocol.SDP
{
    [DataContract]
    [SCFSerializable]
    class HandclaspProtocol
    {
        /**
	     * 权限认证类型(1、客户端发送公钥至服务器 2、客户端发送授权文件密文至服务器)
	     */
        private String _type;
        /**
	     * 信息内容
	     */
        private String _data;

        /**
	     * 实例化HandclaspProtocol
	     * @param type ("1"表示客户端发送公钥至服务器 "2"表示客户端发送授权文件密文至服务器)
	     * @param data (密文)
	     */
        public HandclaspProtocol(String type, String data)
        {
            _type = type;
            _data = data;
        }

        [DataMember(Name = "type")]
        [SCFMember]
        public String Type
        {
            get { return _type; }
            set { _type = value; }
        }

        [DataMember(Name = "date")]
        [SCFMember]
        public String Date
        {
            get { return _data; }
            set { _data = value; }
        }
    }
}
