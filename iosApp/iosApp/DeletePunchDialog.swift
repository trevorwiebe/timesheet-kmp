//
//  DeleteTimeDialog.swift
//  iosApp
//
//  Created by Trevor Wiebe on 3/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI

struct DeletePunchDialog: View {
    var onDelete: () -> Void

    @State private var showDialog = false

    func onDismiss() {
        showDialog = false
    }

    func onConfirm() {
        onDelete()
        showDialog = false
    }

    var body: some View {
        DestructiveButton(text: "Delete Time") {
            showDialog = true
        }
        .alert("Are you sure?", isPresented: $showDialog) {
            Button("Cancel", role: .cancel) { onDismiss() }
            Button("Delete", role: .destructive) { onConfirm() }
        } message: {
            Text("Are you sure you want to delete this time? This will remove the punch-in and punch-out time.")
        }
    }
}
