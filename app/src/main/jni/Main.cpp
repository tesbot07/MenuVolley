/*
 * Credits:
 *
 * Octowolve - Mod menu: https://github.com/z3r0Sec/Substrate-Template-With-Mod-Menu
 * And hooking: https://github.com/z3r0Sec/Substrate-Hooking-Example
 * VanHoevenTR A.K.A Nixi: https://github.com/LGLTeam/VanHoevenTR_Android_Mod_Menu
 * MrIkso - Mod menu: https://github.com/MrIkso/FloatingModMenu
 * Rprop - https://github.com/Rprop/And64InlineHook
 * MJx0 A.K.A Ruit - KittyMemory: https://github.com/MJx0/KittyMemory
 * */
#include <list>
#include <vector>
#include <string.h>
#include <pthread.h>
#include <cstring>
#include <jni.h>
#include <unistd.h>
#include <fstream>
#include "Includes/base64.hpp"
#include "KittyMemory/MemoryPatch.h"
#include "Includes/Logger.h"
#include "Includes/Utils.h"
#include "Includes/obfuscate.h"

#include "Menu/Sounds.h"
#include "Menu/Menu.h"

#include "Toast.h"

#if defined(__aarch64__) //Compile for arm64 lib only
#include <And64InlineHook/And64InlineHook.hpp>
#else //Compile for armv7 lib only. Do not worry about greyed out highlighting code, it still works

#include <Substrate/SubstrateHook.h>
#include <Substrate/CydiaSubstrate.h>

#endif

bool loggedin = false;

extern "C"
JNIEXPORT void JNICALL
Java_uk_lgl_StaticAct_Check(JNIEnv *env, jclass clazz) {
    loggedin = true;
}

// fancy struct for patches for kittyMemory
struct My_Patches {
    MemoryPatch AimBot,AimBot2,Wide,Hair,LessRecoil,NoFog;
    MemoryPatch Magic,Flash,Flash2,BlackSky,AntiShake,AntiShake2,NoGrass,LongJump;
	MemoryPatch Tess,DarkMap,FastShot,Instant,FastChange;
} hexPatches;

bool f1,f2,f3,f4,f5,f6,f7,f8,f9,f10,f11,f12,f13 = false;
bool feature1 = false, feature2 = false, featureHookToggle = false;


//Target lib here
#define targetLibName OBFUSCATE("libUE4.so")

extern "C" {
JNIEXPORT void JNICALL
Java_uk_lgl_modmenu_Preferences_Changes(JNIEnv *env, jclass clazz, jobject obj,
                                        jint feature, jint value, jboolean boolean, jstring str) {

    const char *featureName = env->GetStringUTFChars(str, 0);
    feature += 1;  // No need to count from 0 anymore. yaaay :)))

    LOGD(OBFUSCATE("Feature name: %d - %s | Value: = %d | Bool: = %d"), feature, featureName, value,
         boolean);
		 
		 switch(feature){
			 case 1:
				 f1 = boolean;
				 if (f1) {
					 hexPatches.AimBot2.Apply();
					 } else {
						 hexPatches.AimBot2.Reset();
						 }
						 break;				 
			 case 2:
				 f2 = boolean;
				 if (f2) {
					 hexPatches.LessRecoil.Apply();
					 } else {
						 hexPatches.LessRecoil.Reset();
						 }
						 break;
			 case 3:
				 f3 = boolean;
				 if (f3) {
					 hexPatches.Magic.Apply();
					 } else {
						 hexPatches.Magic.Reset();
						 }
						 break;
			 case 4:
				 f4 = boolean;
				 if (f4) {
					 hexPatches.AntiShake.Apply();
					 hexPatches.AntiShake2.Apply();
					 } else {
						 hexPatches.AntiShake.Reset();
						 hexPatches.AntiShake2.Reset();
						 }
						 break;
			 case 5:
				 f5 = boolean;
				 if (f5) {
					 hexPatches.Hair.Apply();
					 } else {
						 hexPatches.Hair.Reset();
						 }
						 break;
			 case 6:
				 f6 = boolean;
				 if (f6) {
					 hexPatches.DarkMap.Apply();
					 } else {
						 hexPatches.DarkMap.Reset();
						 }
						 break;
			 case 7:
				 f7 = boolean;
				 if (f7) {
					 hexPatches.NoFog.Apply();
					 } else {
						 hexPatches.NoFog.Reset();
						 }
			 case 8:
				 f8 = boolean;
				 if (f8) {
					 hexPatches.BlackSky.Apply();
					 } else {
						 hexPatches.BlackSky.Reset();
						 }
						 break;
 			 case 9:
				 f9 = boolean;
				 if (f9) {
					 hexPatches.NoGrass.Apply();
					 } else {
						 hexPatches.NoGrass.Reset();
						 }
						 break;
			 case 10:
				 f10 = boolean;
				 if (f10) {
					 hexPatches.Wide.Apply();
					 } else {
						 hexPatches.Wide.Reset();
						 }
						 break;
			 case 11:
				 f11 = boolean;
				 if (f11) {
					 hexPatches.LongJump.Apply();
					 } else {
						 hexPatches.LongJump.Reset();
						 }
						 break;
			 case 12:
				 f12 = boolean;
				 if (f12) {
					 hexPatches.Instant.Apply();
					 } else {
						 hexPatches.Instant.Reset();
						 }
						 break;	 
				}
}
}

//
void *hack_thread(void *) {
    LOGI(OBFUSCATE("pthread called"));

    //Check if target lib is loaded
    do {
        sleep(1);
    } while (!isLibraryLoaded(targetLibName));

    LOGI(OBFUSCATE("%s has been loaded"), (const char *) targetLibName);

    hexPatches.AimBot = MemoryPatch(targetLibName, string2Offset("0x24A73F0"), "\x00\x00\x00\x00", 4);
    hexPatches.AimBot2 = MemoryPatch(targetLibName, 0x24A74B0, "\x00\x00\x00\x00", 4);
    hexPatches.LessRecoil = MemoryPatch(targetLibName, 0x130B4D0 , "\x00\x00\x00\x00", 4);//E3224
    hexPatches.NoFog = MemoryPatch(targetLibName, 0x2C344C8 , "\x00\x00\x00\x00", 4);
    hexPatches.Wide = MemoryPatch(targetLibName, 0x37307E0, "\x00\x00\x70\x43", 4);    
    hexPatches.Hair = MemoryPatch(targetLibName, 0x1B62FE0, "\x94\x65\x08\x00", 4);
  
    hexPatches.Magic = MemoryPatch(targetLibName, 0x3B65608, "\x00\x00\x8C\x42", 4);
    hexPatches.Flash = MemoryPatch(targetLibName, 0x3789924, "\x00\x00\x00\x00", 4);
    hexPatches.Flash2 = MemoryPatch(targetLibName, 0x3789A4C, "\x00\x00\x00\x00", 4);
    
    hexPatches.BlackSky = MemoryPatch(targetLibName, 0x39E0C50, "\xB0\xC6\x27\xB7", 4);
    
    hexPatches.AntiShake = MemoryPatch(targetLibName, 0x367EC84, "\x00\x00\x00\x00", 4);
    hexPatches.AntiShake2 = MemoryPatch(targetLibName, 0x372D818, "\x00\x00\x00\x00", 4);
    
    hexPatches.NoGrass = MemoryPatch(targetLibName, 0x2475D58, "\x00\x00\x00\x00", 4);
    
    hexPatches.LongJump = MemoryPatch(targetLibName, 0x1150134, "\x02\x1A\xB7\xEE", 4); //021AB7EE
	hexPatches.FastShot = MemoryPatch(targetLibName, 0x37C1BA4, "\x00\x00\x00\x00", 4); //295C8F3D
    hexPatches.DarkMap = MemoryPatch(targetLibName, 0x2E082FC, "\x00\x00\x00\x00", 4); //295C8F3D
    hexPatches.Instant = MemoryPatch(targetLibName, 0x37C1BA4, "\x00\x00\x00\x00", 4); //295C8F3D
    hexPatches.FastChange = MemoryPatch(targetLibName, 0x1BB7C74, "\x00\x00\x00\x00", 4); //295C8F3D
    
	//*/
    return NULL;
}

//No need to use JNI_OnLoad, since we don't use JNIEnv
//We do this to hide OnLoad from disassembler
__attribute__((constructor))
void lib_main() {
    // Create a new thread so it does not block the main thread, means the game would not freeze
    pthread_t ptid;
    pthread_create(&ptid, NULL, hack_thread, NULL);
}

