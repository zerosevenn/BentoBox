package com.zerosevenn.bentobox.listeners;

import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryInteractListener implements Listener {

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        InventoryView view = event.getView();
        if(view.getTitle().equalsIgnoreCase("")){
            return;
        }
    }

}
