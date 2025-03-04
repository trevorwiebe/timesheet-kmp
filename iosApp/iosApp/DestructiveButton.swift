//
//  DestructiveButton.swift
//  iosApp
//
//  Created by Trevor Wiebe on 3/3/25.
//  Copyright © 2025 orgName. All rights reserved.
//

import SwiftUI
import ComposeApp

struct DestructiveButton: View {
    var text: String
    var action: () -> Void
    
    var body: some View{
        Button(
            role: .destructive,
            action: action,
            label: {
                Text(text).font(.headline)
            }
        ).buttonStyle(.bordered)
            .controlSize(.regular)
            .clipShape(Capsule())
    }
}
