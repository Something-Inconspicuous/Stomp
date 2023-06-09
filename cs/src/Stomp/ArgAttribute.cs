
using System;

namespace Stomp {
    [AttributeUsage(AttributeTargets.Field, Inherited = false, AllowMultiple = true)]
    public sealed class ArgAttribute : Attribute {
        public ArgAttribute(object defualtValue, string LongName, string ShortName) {
            DefualtValue = defualtValue;
        }

        public object DefualtValue { get; set; }

        public string? LongName { get; set; }
        public string? ShortName { get; set; }
    }
}