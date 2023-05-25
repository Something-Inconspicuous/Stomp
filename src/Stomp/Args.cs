
using System;
using System.Reflection;

namespace Stomp {
    public abstract class AbstractArgs {
        /// <summary>
        /// Parses the arguments into an <see cref="AbstractArgs"/> derived class
        /// </summary>
        /// <param name="args">The arguments to parse</param>
        /// <returns>The parsed arguments</returns>
        public static A Parse<A>(string[] args) where A : AbstractArgs {
            FieldInfo[] Afields = typeof(A).GetFields();
            //Dictionary<string, FieldInfo> fieldNames = new Dictionary<string, FieldInfo>();

            for (int i = 0; i < Afields.Length; i++) {
                ArgAttribute argAttribute = Afields[i].GetCustomAttribute<ArgAttribute>();
                
                if(argAttribute.LongName is not null){
                    bool has = ArgsHave(args, argAttribute.LongName, argAttribute.ShortName);
                    if(has){
                        Afields[i] = args[i + 1];
                        i++; // we have already taken the very next argument, so we can skip it
                    }
                    
                }
            }
        }

        private static bool ArgsHave(string[] args, string? arg1, string? arg2){
            if(arg1 == null && arg2 == null) return false;
            for (int i = 0; i < args.Length; i++)
                if(args[i] == arg1) return true;
                else if(args[i] == arg2) return true;
            return false;
        }
    }
}