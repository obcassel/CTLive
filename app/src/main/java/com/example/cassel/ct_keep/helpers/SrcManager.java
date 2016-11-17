package com.example.cassel.ct_keep.helpers;

/**
 * Gerenciador do cálculo do CRC
 */
public class SrcManager {

    /**
     * Funcao de cálculo do CRC32,
     *
     * @param string String a ser transformada
     * @return integer CRC calculado
     * @autor Robert Sedgewick and Kevin Wayne.
     */
    public Integer calcularCRC(String string) {
        int crc = 0xFFFFFFFF;       // initial contents of LFBSR
        int poly = 0xEDB88320;      // reverse polynomial
        byte bytes[] = string.getBytes();

        for (byte b : bytes) {
            int temp = (crc ^ b) & 0xff;

            // read 8 bits one at a time
            for (int i = 0; i < 8; i++) {
                if ((temp & 1) == 1) {
                    temp = (temp >>> 1) ^ poly;
                } else {
                    temp = (temp >>> 1);
                }
            }
            crc = (crc >>> 8) ^ temp;
        }

        // flip bits
        crc = crc ^ 0xffffffff;
        return crc;
    }
}
