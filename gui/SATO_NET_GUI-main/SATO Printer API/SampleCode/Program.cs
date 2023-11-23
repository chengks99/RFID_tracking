using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace SampleCode
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main(string[] args)
        {
            string action = "";
            if (args == null || args.Length == 0)
            {
                // no arguments
            }
            else
            {
                action = args[0].ToString();
            }

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Application.Run(new SampleForm(action));
        }
    }
}
