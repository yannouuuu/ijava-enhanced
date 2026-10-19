package org.h2.value;

import java.nio.charset.Charset;
import java.text.CollationKey;
import java.text.Collator;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: ijava.jar:org/h2/value/CharsetCollator.class */
public class CharsetCollator extends Collator {
    private final Charset charset;

    public CharsetCollator(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return this.charset;
    }

    @Override // java.text.Collator
    public int compare(String str, String str2) {
        return Arrays.compare(toBytes(str), toBytes(str2));
    }

    byte[] toBytes(String str) {
        if (getStrength() <= 1) {
            str = str.toUpperCase(Locale.ROOT);
        }
        return str.getBytes(this.charset);
    }

    @Override // java.text.Collator
    public CollationKey getCollationKey(String str) {
        return new CharsetCollationKey(str);
    }

    @Override // java.text.Collator
    public int hashCode() {
        return 255;
    }

    /* loaded from: ijava.jar:org/h2/value/CharsetCollator$CharsetCollationKey.class */
    private class CharsetCollationKey extends CollationKey {
        private final byte[] bytes;

        CharsetCollationKey(String str) {
            super(str);
            this.bytes = CharsetCollator.this.toBytes(str);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.Comparable
        public int compareTo(CollationKey collationKey) {
            return Arrays.compare(this.bytes, collationKey.toByteArray());
        }

        @Override // java.text.CollationKey
        public byte[] toByteArray() {
            return this.bytes;
        }
    }
}
