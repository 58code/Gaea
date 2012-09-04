using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml;

namespace WWW58COM.SPAT.SCF.Client.Configuration.Secure
{
    class KeyProfile
    {
        public KeyProfile(XmlNode node)
        {
            if (node != null)
            {
                XmlAttribute atribute = node.Attributes["name"];
                if (atribute != null)
                {
                    this.info = atribute.Value;
                }
            }
        }
        /**
	     * 授权文件
	     */
        public String info
        {
            get;
            set;
        }
    }
}
