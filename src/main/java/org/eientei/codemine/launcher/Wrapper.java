package org.eientei.codemine.launcher;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.util.Arrays;

public class Wrapper {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PrintStream out = System.out;
        URL.setURLStreamHandlerFactory(new URLStreamHandlerFactory() {
            @Override
            public URLStreamHandler createURLStreamHandler(String protocol) {
                if (protocol.equals("https")) {
                    return new sun.net.www.protocol.https.Handler() {
                        @Override
                        protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
                            out.println("WRAPP " + url);
                            try {
                                if (url.getHost().equals("sessionserver.mojang.com")) {
                                    if (url.toString().contains("/hasJoined?")) {
                                        url = new URL(args[1] + url.toString().replaceAll(".*username=([^&]*).*", "$1"));
                                    } else {
                                        url = new URL(args[1] + url.getPath().replaceAll(".*/", ""));
                                    }
                                }
                                if (url.getHost().equals("mock.mojang.com")) {
                                    url = new URL(url.toString().replace("mock.mojang.com", args[2]));
                                }
                                return super.openConnection(url, proxy);
                            } catch (Exception e) {
                                throw new IOException(e);
                            }
                        }
                    };
                } else if (protocol.equals("http")) {
                    return new sun.net.www.protocol.http.Handler() {
                        @Override
                        protected URLConnection openConnection(URL url, Proxy proxy) throws IOException {
                            out.println("WRAPPHTTP " + url);
                            return super.openConnection(url, proxy);
                        }
                    };
                }
                return null;
            }
        });

        String actualMain = args[0];
        String[] nargs = Arrays.copyOfRange(args, 3, args.length);
        Class<?> mainClass = Class.forName(actualMain);
        Method mainMethod = mainClass.getMethod("main", String[].class);
        mainMethod.invoke(null, (Object) nargs);
    }
}
