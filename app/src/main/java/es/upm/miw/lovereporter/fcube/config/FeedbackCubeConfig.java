/*******************************************************************************
 * Copyright (C) 2014 Open University of The Netherlands
 * Author: Bernardo Tabuenca Archilla
 * Lifelong Learning Hub project 
 *
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package es.upm.miw.lovereporter.fcube.config;

public class FeedbackCubeConfig {

    /**
     * > GET / HTTP/1.1                      : Responds w/ "Hello from Arduino Server"
     * > GET /ring/color/ HTTP/1.1           : Responds w/ a JSON representation of
     * the strip color -> {"r":x,"g":x,"b":x}
     * > PUT /ring/on/ HTTP/1.1              : Turns the LED strip on
     * > PUT /ring/off/ HTTP/1.1             : Turns the LED strip off
     */

    public static final String URL_PREFIX = "http://";

    private static FeedbackCubeConfig singleInstance;

    private String ip_address = "192.168.0.100";

    public static FeedbackCubeConfig getSingleInstance() {
        if (singleInstance == null) {
            synchronized (FeedbackCubeConfig.class) {
                if (singleInstance == null) {
                    singleInstance = new FeedbackCubeConfig();
                }
            }
        }
        return singleInstance;
    }

    public String getIp() {
        return ip_address;
    }
}
