package com.admission.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class Test {
    public static void main(String[] args) {
        /*
        123
        jwt =  1c1f4f0008c032a01dbb17bddaca9b7a
        bcry = $2a$10$plH5VG3Vb2Iwhrd8T8RaUuQ/rwvBTfmoVSMU40eevYbrFrjMeURF2

        admin
        jwt = 6607f51871353c72ad66bfcb21635565
        bcry = $2a$10$XwflipXp2ejxDxqpClqLd.rP4dn3dRPub5vaB4i8RCODrirOs3PVq
         */
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.out.println(passwordEncoder.encode("admin"));
    }
}
