package unisannino.denenderman;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDenEnderBlock extends GuiContainer
{
    public GuiDenEnderBlock(InventoryPlayer iinventory, TileEntityDenEnder iinventory1)
    {
        super(new ContainerDenEnder(iinventory, iinventory1));
        inventoryRows = 0;
        upperChestInventory = iinventory;
        lowerChestInventory = iinventory1;
        allowUserInput = false;
        char c = '\336';
        int i = c - 108;
        inventoryRows = iinventory1.getSizeInventory() / 9;
        ySize = i + inventoryRows * 18;
    }

    protected void drawGuiContainerForegroundLayer()
    {
        fontRenderer.drawString(StatCollector.translateToLocal(lowerChestInventory.getInvName()), 8, 6, 0x404040);
        fontRenderer.drawString(StatCollector.translateToLocal(upperChestInventory.getInvName()), 8, (ySize - 96) + 2, 0x404040);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
    {
        int i1 = mc.renderEngine.getTexture("/gui/container.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i1);
        int j1 = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j1, k, 0, 0, xSize, inventoryRows * 18 + 17);
        drawTexturedModalRect(j1, k + inventoryRows * 18 + 17, 0, 126, xSize, 96);
    }

    private IInventory upperChestInventory;
    private IInventory lowerChestInventory;
    private TileEntityDenEnder denender;
    private int inventoryRows;
}
