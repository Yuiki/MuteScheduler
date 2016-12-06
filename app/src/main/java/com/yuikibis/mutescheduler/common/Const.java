package com.yuikibis.mutescheduler.common;

public class Const {
    public enum AlertDialogTag {
        ADD,
    }
    
    public enum ContextMenuItem {
        ON_OFF(0), DELETE(1), NULL(2);

        private final int num;
        
        ContextMenuItem(int num) {
            this.num = num;
        }

        public int toInt() {
            return num;
        }

        public static ContextMenuItem valueOf(int value) {
            for (ContextMenuItem item : values()) {
                if (item.toInt() == value) {
                    return item;
                }
            }
            return NULL;
        }
    }
}
