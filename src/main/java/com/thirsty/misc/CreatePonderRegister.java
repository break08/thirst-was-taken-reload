package com.thirsty.misc;

public class CreatePonderRegister {
    public static void init(){
        net.createmod.ponder.foundation.PonderIndex.addPlugin(new com.thirsty.api.create.ponder.ThirstPonderPlugin());
    }
}
