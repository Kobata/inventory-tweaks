package invtweaks.integration;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;

import org.jetbrains.annotations.NotNull;

import invtweaks.InvTweaks;
import invtweaks.forge.CommonProxy;
import invtweaks.forge.InvTweaksMod;;

public class ItemListSorter {
	@Mod.Instance
    public static InvTweaksMod instance;

    @SidedProxy(clientSide = "invtweaks.forge.ClientProxy", serverSide = "invtweaks.forge.CommonProxy")
    public static CommonProxy proxy;
	
	
	public static class ItemListComparator implements Comparator<ItemStack>
	{

		@Override
		public int compare(ItemStack o1, ItemStack o2) {
			if (o1 == null && o2 == null)
				return 0;
			else if (o1 == null)
				return 1;
			else if (o2 == null)
				return -1;
			return InvTweaks.getInstance().compareItems(o1, o2, true);
		}
		
	}
	
	public static void LinkJEITComparator()
	{
		if (Loader.isModLoaded("jei")) {
			try {
				Class<?> ingredientListElementComparator = Class.forName("mezz.jei.ingredients.IngredientListElementComparator");
				Class<?> clientConfig = Class.forName("mezz.jei.config.Config");
				if (ingredientListElementComparator != null && clientConfig != null) {
					Class[] cArg = new Class[2];
					cArg[0] = String.class;
					cArg[1] = Comparator.class;
					Method addItemStackComparison = ingredientListElementComparator.getMethod("addItemStackComparison", cArg);
					Method updateSortOrder = clientConfig.getMethod("updateSortOrder");
					if (addItemStackComparison != null && updateSortOrder != null) {
						Object[] oArg = new Object[2];
						oArg[0] = "invtweaks";
						oArg[1] = new ItemListComparator();
						addItemStackComparison.invoke(null, oArg);
						updateSortOrder.invoke(null);
					}
				}
			} catch (@NotNull ClassNotFoundException | NoSuchMethodException | SecurityException | InvocationTargetException | IllegalAccessException ignored) {
				return;
			}
		}
	}

}
