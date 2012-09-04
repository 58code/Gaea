using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Test
{
    class Program
    {
        static void Main(string[] args)
        {

            string url = "tcp://demo/NewsService";
            scfDemo.contract.INewsService up = Com.Bj58.Spat.Gaea.Client.Proxy.Builder.ProxyFactory.Create<scfDemo.contract.INewsService>(url);
            var list = up.getNewsByCateID();

            Console.WriteLine(list.Count);
            Console.WriteLine(list.Count > 0 ? "" : list[0].title);
            Console.ReadLine();
        }
    }
}
