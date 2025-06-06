package me.miki.shindo.mobends.animation.player;

import me.miki.shindo.mobends.animation.MoBendsAnimation;
import me.miki.shindo.mobends.client.model.ModelRendererBends;
import me.miki.shindo.mobends.client.model.entity.ModelBendsPlayer;
import me.miki.shindo.mobends.data.MoBends_EntityData;
import me.miki.shindo.mobends.util.MoBendsGUtil;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class Animation_Bow extends MoBendsAnimation{
	
	public String getName(){
		return "bow";
	}

	@Override
	public void animate(EntityLivingBase argEntity, ModelBase argModel, MoBends_EntityData argData) {
		ModelBendsPlayer model = (ModelBendsPlayer) argModel;
		EntityPlayer player = (EntityPlayer) argEntity;
		
   		float aimedBowDuration = 0;
		
    	if(player != null){
    		aimedBowDuration = player.getItemInUseDuration();
    	}
    	
    	if(aimedBowDuration > 15){
    		aimedBowDuration = 15;
    	}
    	
    	if(aimedBowDuration < 10){
    		((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(0,0.3f);
    		((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(0,0.3f);
    		
    		((ModelRendererBends)model.bipedBody).rotation.setSmoothX(30,0.3f);
    		((ModelRendererBends)model.bipedBody).rotation.setSmoothY(0,0.3f);
    		
    		((ModelRendererBends)model.bipedRightArm).rotation.setSmoothZ(0);
    		((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-30);
    		((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-0-30);
    		
    		((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(80.0f);
    		
    		float var = (aimedBowDuration/10.0f);
    		((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(var*-50.0f);
    		
    		((ModelRendererBends)model.bipedHead).rotation.setSmoothX(model.headRotationX-30,0.3f);
    	}else{
    		float var1 = 20-(((aimedBowDuration-10))/5.0f)*20;
    		((ModelRendererBends)model.bipedBody).rotation.setSmoothX(var1,0.3f);
    		float var = ((aimedBowDuration-10)/5.0f)*-25;
    		((ModelRendererBends)model.bipedBody).rotation.setSmoothY(var+model.headRotationY,0.3f);
    		
    		((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(-90-var1,0.3f);
    		((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothX(-0-30);
    		
    		((ModelRendererBends)model.bipedLeftArm).rotation.setSmoothY(80.0f);
    		
    		float var2 = (aimedBowDuration/10.0f);
    		((ModelRendererBends)model.bipedLeftForeArm).rotation.setSmoothX(var2*-30.0f);
    		
    		((ModelRendererBends)model.bipedRightArm).pre_rotation.setSmoothY(var);
    		
    		float var5 = -90+model.headRotationX;
    		var5 = MoBendsGUtil.min(var5, -120);
    		((ModelRendererBends)model.bipedLeftArm).pre_rotation.setSmoothX(var5,0.3f);
    		
    		((ModelRendererBends)model.bipedRightArm).rotation.setSmoothX(model.headRotationX-90.0f);
    		
    		((ModelRendererBends)model.bipedHead).rotation.setY(-var);
    		((ModelRendererBends)model.bipedHead).pre_rotation.setSmoothX(-var1,0.3f);
    		((ModelRendererBends)model.bipedHead).rotation.setX(model.headRotationX);
    	}
	}
}
